"use client";

import ContactHeroSection from "@/organisms/ContactHeroSection";
import ContactFormCard from "@/organisms/ContactFormCard";
import ContactMapSection from "@/organisms/ContactMapSection";
import ContactTemplate from "@/templates/ContactTemplate";

import { usePageTitle } from "@/hooks/usePageTitle";

export default function ContactPage() {
    usePageTitle("Contactanos");

    return (
        <ContactTemplate>
            <ContactHeroSection>
                <ContactFormCard />
            </ContactHeroSection>
            <ContactMapSection />
        </ContactTemplate>
    );
}
