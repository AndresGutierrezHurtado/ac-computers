"use client";

import { useEffect } from "react";
import ContactTemplate from "@/templates/ContactTemplate";
import ContactHeroSection from "@/organisms/ContactHeroSection";
import ContactFormCard from "@/organisms/ContactFormCard";
import ContactMapSection from "@/organisms/ContactMapSection";

export default function ContactPage() {
    useEffect(() => {
        document.title = "Contactanos | AC Computers";
    }, []);

    return (
        <ContactTemplate>
            <ContactHeroSection>
                <ContactFormCard />
            </ContactHeroSection>
            <ContactMapSection />
        </ContactTemplate>
    );
}
