import ContactInfoItem from "@/atoms/ContactInfoItem";
import {
    AtIcon,
    FacebookIcon,
    InstagramIcon,
    LocationIcon,
    WhatsappIcon,
} from "@/atoms/Icons";

const SOCIAL_MEDIAS = [
    {
        icon: <InstagramIcon size={20} />,
        url: "https://www.instagram.com/accomputersas/",
        text: "Instagram: @accomputersas",
    },
    {
        icon: <FacebookIcon size={20} />,
        url: "https://web.facebook.com/Amaliacastrode",
        text: "Facebook: Amalia Castro Ardila",
    },
    {
        icon: <WhatsappIcon size={20} />,
        url: "https://wa.me/+573118835868",
        text: "WhatsApp: 311 8835868",
    },
    {
        icon: <AtIcon size={20} />,
        url: "mailto:amaliacastro78@example.com",
        text: "Correo: amaliacastro78@gmail.com",
    },
    {
        icon: <LocationIcon size={20} />,
        url: "https://maps.app.goo.gl/qHg7MtCmABNfReT8A",
        text: "Ubicacion: Cra. 15 #77 05, Bogota",
    },
];

export default function ContactHeroSection({ children }) {
    return (
        <section className="w-full px-3">
            <div className="w-full max-w-[1200px] mx-auto mt-[100px]">
                <div className="flex flex-col lg:flex-row gap-10 w-full">
                    <div className="w-full lg:w-1/2 space-y-6">
                        <div className="space-y-3">
                            <h1 className="text-4xl font-extrabold tracking-tight">
                                Contactanos
                            </h1>
                            <p className="text-base-content/80">
                                Lorem ipsum dolor sit, amet consectetur adipisicing elit.
                                Suscipit corrupti qui adipisci eius dignissimos sunt quo libero,
                                quae voluptates magnam temporibus in reprehenderit voluptatum
                                porro animi dolor. Minima, repudiandae at reiciendis obcaecati,
                                necessitatibus ducimus odio fugit iure quaerat natus amet modi!
                                Ipsam, tempore! Odit ducimus molestias dolor neque doloribus
                                qui.
                            </p>
                        </div>
                        <ol className="space-y-2.5">
                            {SOCIAL_MEDIAS.map((socialMedia) => (
                                <ContactInfoItem
                                    key={socialMedia.text}
                                    icon={socialMedia.icon}
                                    url={socialMedia.url}
                                    text={socialMedia.text}
                                />
                            ))}
                        </ol>
                    </div>
                    <div className="w-full lg:w-1/2">{children}</div>
                </div>
            </div>
        </section>
    );
}
