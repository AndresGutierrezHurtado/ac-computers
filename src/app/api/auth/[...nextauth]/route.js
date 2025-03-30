import bcrypt from "bcrypt";
import NextAuth from "next-auth";
import GoogleProvider from "next-auth/providers/google";
import FacebookProvider from "next-auth/providers/facebook";
import CredentialsProvider from "next-auth/providers/credentials";
import SequelizeAdapter from "@auth/sequelize-adapter";

import * as models from "@/database/models";

const handler = NextAuth({
    providers: [
        CredentialsProvider({
            name: "Credentials",
            credentials: {
                user_email: {
                    label: "Correo electrónico",
                    type: "email",
                    placeholder: "ejemplo@gmail.com",
                },
                user_password: {
                    label: "Contraseña",
                    type: "password",
                    laceholder: "*******",
                },
            },
            async authorize(credentials) {
                const { user_email, user_password } = credentials;
                const user = await models.User.findOne({ where: { user_email } });

                if (!user) {
                    throw new Error("El usuario no existe.");
                }

                const isPasswordValid = await bcrypt.compare(user_password, user.user_password);

                if (!isPasswordValid) {
                    throw new Error("La contraseña es incorrecta.");
                }

                const result = user.toJSON();
                return {
                    ...result,
                    role: result.role_id,
                };
            },
        }),
        GoogleProvider({
            clientId: process.env.GOOGLE_CLIENT_ID,
            clientSecret: process.env.GOOGLE_CLIENT_SECRET,
        }),
        FacebookProvider({
            clientId: process.env.FACEBOOK_APP_ID,
            clientSecret: process.env.FACEBOOK_APP_SECRET,
        }),
    ],
    adapter: SequelizeAdapter(models.connection),
    secret: process.env.SESSION_SECRET,
    session: {
        strategy: "jwt",
        maxAge: 30 * 24 * 60 * 60,
        updateAge: 24 * 60 * 60,
    },
    cookies: {
        sessionToken: {
            name: "next-auth.session-token",
            options: {
                httpOnly: true,
                sameSite: "lax",
                path: "/",
                secure: false,
            },
        },
    },
    callbacks: {
        async signIn({ user, account, profile }) {
            if (account.provider === "google") {
                if (!profile.email) {
                    throw new Error("La cuenta de Google no tiene email asociado.");
                }

                await models.User.findOrCreate({
                    where: { user_email: profile.email },
                    defaults: {
                        user_email: profile.email,
                        user_name: profile.given_name,
                        user_lastname: profile.family_name,
                        user_password: profile.sub,
                    },
                });
            }
            if (account.provider === "facebook") {
                if (!profile.email) {
                    throw new Error("La cuenta de Facebook no tiene email asociado.");
                }

                await models.User.findOrCreate({
                    where: { user_email: profile.email },
                    defaults: {
                        user_email: profile.email,
                        user_name: profile.first_name,
                        user_lastname: profile.last_name,
                        user_password: profile.id,
                    },
                });
            }
            return true;
        },
        async jwt({ token, user }) {
            if (user && user.user_email) {
                token.id = user.id;
                token.name = user.user_name + " " + user.user_lastname;
                token.email = user.user_email;
                token.role = user.role;
            }

            if (!token.role) {
                const dbUser = await models.User.findOne({ where: { user_email: token.email } });

                token.role = dbUser.role_id;
            }

            return token;
        },
        async session({ session, token }) {
            session.user = await models.User.findOne({
                where: { user_email: token.email },
                attributes: {
                    exclude: ["user_password"],
                },
                include: ["role"],
            });

            return session;
        },
        async redirect({ url, baseUrl }) {
            return baseUrl;
        },
    },
});

export { handler as GET, handler as POST };
