export default function Button({
    children,
    leftIcon = null,
    rightIcon = null,
    onClick,
    type = "button",
    disabled = false,
    className = "",
    ...props
}) {
    const base = "btn";

    return (
        <button
            type={type}
            onClick={onClick}
            disabled={disabled}
            className={`${base} ${className}`.trim()}
            {...props}
        >
            <span className="flex items-center gap-2">
                {leftIcon && <span className="flex items-center">{leftIcon}</span>}
                {children && <span>{children}</span>}
                {rightIcon && <span className="flex items-center">{rightIcon}</span>}
            </span>
        </button>
    );
}
