import ContactInfoItem from "@/atoms/ContactInfoItem";
import { AtIcon, FacebookIcon, InstagramIcon, LocationIcon, WhatsappIcon } from "@/atoms/Icons";

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
                            <h1 className="text-4xl font-extrabold tracking-tight">Contactanos</h1>
                            <p className="text-base-content/80">
                                AC Computers es una tienda especializada en mantenimiento y
                                reparación de computadores, así como en servicio técnico para
                                dispositivos móviles en Bogotá. Ofrece soluciones orientadas a
                                diagnosticar, optimizar y prolongar la vida útil de equipos,
                                abarcando desde problemas de hardware hasta configuraciones de
                                software. Su enfoque se centra en brindar atención confiable,
                                tiempos de respuesta eficientes y soporte técnico adaptado a las
                                necesidades tanto de usuarios individuales como de pequeñas
                                empresas.
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
