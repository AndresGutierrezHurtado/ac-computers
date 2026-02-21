"use client";
import Modelo3D from "@/organisms/Model";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/all";
import { Children, cloneElement, useEffect, useRef, useState } from "react";

gsap.registerPlugin(ScrollTrigger);

export default function HomeTemplate({ children }) {
    const mainRef = useRef();
    const sceneRef = useRef();
    const sectionRefs = useRef([]);

    const [progress, setProgress] = useState(0);

    useEffect(() => {
        document.title = "Inicio | AC Computers";

        gsap.set(sectionRefs.current[1], { x: "150%", opacity: "-3.5" });
        gsap.set(sectionRefs.current[2], { x: "-150%", opacity: "-3.5" });
        gsap.set(sectionRefs.current[3], { x: "150%", opacity: "-3.5" });

        gsap.timeline({
            scrollTrigger: {
                trigger: mainRef.current,
                start: "top top",
                end: "bottom bottom",
                scrub: 1,
                onUpdate: (self) => setProgress(self.progress),
            },
        })
            .to(sceneRef.current, { x: "-43%", opacity: 1, ease: "none" })
            .to(sceneRef.current, { x: "3%", ease: "none" })
            .to(sceneRef.current, { x: "-43%", ease: "none" });

        gsap.timeline({
            scrollTrigger: {
                trigger: mainRef.current,
                start: "top top",
                end: "bottom bottom",
                scrub: 1,
                onUpdate: (self) => setProgress(self.progress),
            },
        })
            .to(sectionRefs.current[1], { x: "0%", opacity: 1, ease: "none" })
            .to(sectionRefs.current[2], { x: "0%", opacity: 1, ease: "none" })
            .to(sectionRefs.current[3], { x: "0%", opacity: 1, ease: "none" });
    }, []);

    return (
        <main
            className="w-full min-h-screen flex flex-col gap-[100px] lg:gap-10 overflow-x-hidden overflow-y-scroll snap-y snap-mandatory"
            ref={mainRef}
        >
            <div
                className="fixed top-[2.5%] right-[-20%] h-screen w-screen hidden lg:block pointer-events-none z-40 snap-center"
                ref={sceneRef}
            >
                <Modelo3D progress={progress} />
            </div>

            {Children.map(children, (child, i) =>
                cloneElement(child, { ref: (el) => (sectionRefs.current[i] = el) }),
            )}
        </main>
    );
}
