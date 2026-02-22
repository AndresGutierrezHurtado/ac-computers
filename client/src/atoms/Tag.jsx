export default function Tag({ children, type = "default" }) {
    const types = {
        default: "bg-base-300 text-base-content",
        primary: "bg-primary/20 text-primary border border-primary/30",
        secondary: "bg-secondary/20 text-secondary border border-secondary/30",
        accent: "bg-accent/20 text-accent border border-accent/30",
    };

    return (
        <span className={`text-xs font-bold px-2 py-1 rounded-md ${types[type] || types.default}`}>
            {children}
        </span>
    );
}
