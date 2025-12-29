import React from "react";

export default function Loading() {
    return (
        <>
            <section className="w-full px-4 mt-[100px]">
                <div className="w-full max-w-xl mx-auto flex flex-col gap-5 justify-center items-center py-10 text-center">
                    <div className="space-y-2">
                        <span className="loading loading-spinner w-50 bg-primary"></span>
                        <h2 className="text-4xl font-extrabold uppercase text-center">
                            AC <span className="text-primary italic">Computers</span>
                        </h2>
                        <p className="text-base-content/80 text-xl ">
                            Por favor, espera un momento.
                        </p>
                    </div>
                </div>
            </section>
        </>
    );
}
