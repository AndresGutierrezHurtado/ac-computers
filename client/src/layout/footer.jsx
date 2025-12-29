import Link from "next/link";
import React from "react";

export default function Footer() {
    return (
        <footer className="footer sm:footer-horizontal footer-center text-base-content p-4">
            <aside>
                <p>
                    Copyright © {new Date().getFullYear()} - All right reserved by{" "}
                    <Link className="hover:underline" href="https://www.linkedin.com/in/andr%C3%A9s-guti%C3%A9rrez-hurtado-25946728b/">
                        Andrés Gutiérrez Hurtado
                    </Link>
                </p>
            </aside>
        </footer>
    );
}
