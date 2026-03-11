export default function AuthField({
    label,
    name,
    type = "text",
    placeholder,
    autoComplete,
    required = true,
    value,
    onChange,
    disabled = false,
}) {
    return (
        <fieldset className="fieldset">
            <label className="fieldset-label font-medium text-base">{label}</label>
            <input
                name={name}
                type={type}
                className="input w-full focus:outline-0 focus:border-primary"
                placeholder={placeholder}
                autoComplete={autoComplete}
                required={required}
                value={value}
                onChange={onChange}
                disabled={disabled}
            />
        </fieldset>
    );
}
