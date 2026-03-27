"use client";

import { useEffect } from "react";

import ProfileCard from "@/organisms/ProfileCard";
import ProfileTemplate from "@/templates/ProfileTemplate";

import { useGetData } from "@/hooks/useClientData";
import { getAuthToken, setAuthSession } from "@/hooks/useAuthSession";
import { usePageTitle } from "@/hooks/usePageTitle";

export default function ProfilePage() {
    const { data: user, loading } = useGetData("/auth/session");

    usePageTitle("Perfil");

    useEffect(() => {
        if (user) {
            setAuthSession({ token: getAuthToken(), user });
        }
    }, [user]);

    return (
        <ProfileTemplate>
            <ProfileCard user={user} loading={loading} />
        </ProfileTemplate>
    );
}
