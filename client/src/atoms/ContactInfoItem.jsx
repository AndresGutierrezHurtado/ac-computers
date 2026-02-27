import Link from "next/link";

export default function ContactInfoItem({ icon, url, text }) {
    return (
        <Link
            href={url}
            target="_blank"
            role="alert"
            className="flex items-center gap-4 bg-black/10 hover:bg-black/20 duration-300 rounded-md p-4"
        >
            <span className="text-primary">{icon}</span>
            <div className="text-sm sm:text-base">{text}</div>
        </Link>
    );
}
