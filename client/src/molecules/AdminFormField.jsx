"use client";

import FormattedNumberInput from "@/atoms/FormattedNumberInput";

export default function AdminFormField({
    label,
    name,
    placeholder,
    type = "text",
    as = "input",
    required = true,
    disabled = true,
    value,
    onChange,
}) {
    const labelClass =
        "label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5";

    return (
        <fieldset className="fieldset">
            <label className="label">
                <span className={required ? labelClass : "label-text font-semibold"}>
                    {label}
                </span>
            </label>
            {as === "textarea" ? (
                <textarea
                    name={name}
                    placeholder={placeholder}
                    className="textarea textarea-sm textarea-bordered focus:textarea-primary focus:outline-0 w-full h-32 resize-none leading-[1.3] disabled:textarea-bordered"
                    disabled={disabled}
                    value={value}
                    onChange={onChange}
                ></textarea>
            ) : type === "number" ? (
                <FormattedNumberInput
                    name={name}
                    placeholder={placeholder}
                    value={value}
                    disabled={disabled}
                    dense={false}
                    onChange={(raw) => onChange?.({ target: { name, value: raw } })}
                />
            ) : (
                <input
                    className="input input-bordered focus:outline-0 focus:input-primary disabled:input-bordered"
                    name={name}
                    type={type}
                    placeholder={placeholder}
                    disabled={disabled}
                    value={value}
                    onChange={onChange}
                />
            )}
        </fieldset>
    );
}
