"use client";

import dynamic from "next/dynamic";

const StoreMap = dynamic(() => import("@/organisms/Map"), { ssr: false });

export default function ContactMapSection() {
    return (
        <section className="w-full px-3">
            <div className="w-full max-w-[1200px] mx-auto py-10">
                <div className="space-y-5">
                    <h2 className="text-4xl font-extrabold tracking-tight">
                        Nuestra ubicación:
                    </h2>
                    <div className="w-full h-[400px] overflow-hidden border rounded-lg">
                        <StoreMap />
                    </div>
                </div>
            </div>
        </section>
    );
}
