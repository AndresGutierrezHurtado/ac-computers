import Link from "next/link";

import { UserPlusIcon } from "@/atoms/Icons";
import Button from "@/atoms/Button";

export default function AuthPromo({ title, description, linkHref, linkLabel, align = "left" }) {
    const alignmentClasses =
        align === "right"
            ? "text-center lg:text-right flex flex-col items-center md:items-end"
            : "text-center lg:text-left flex flex-col items-center lg:items-start";

    return (
        <div className={`${alignmentClasses} gap-4`}>
            <h1 className="text-4xl font-extrabold text-nowrap">{title}</h1>
            <p className="pb-3 text-balance text-lg max-w-lg">{description}</p>
            <Link href={linkHref}>
                <Button
                    className="btn-primary btn-outline btn-wide font-medium"
                    leftIcon={<UserPlusIcon size={16} />}
                >
                    {linkLabel}
                </Button>
            </Link>
        </div>
    );
}
