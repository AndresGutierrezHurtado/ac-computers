"use client";

import { useEffect } from "react";

export const usePageTitle = (title) => {
    useEffect(() => {
        if (typeof document === "undefined") return;

        const baseTitle = "AC Computers";
        document.title = title ? `${title} | ${baseTitle}` : baseTitle;
    }, [title]);
};
