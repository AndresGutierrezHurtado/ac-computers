"use client";

export default function AdminPageTemplate({ children }) {
    return (
        <section className="w-full px-3">
            <div className="w-full max-w-[1200px] mx-auto py-10 mt-[100px]">
                {children}
            </div>
        </section>
    );
}
