export default function ProfileField({ label, value, required = false }) {
    const labelClass = required
        ? "fieldset-label text-sm after:content-['*'] after:text-red-500"
        : "block text-base-content/70 font-semibold";

    return (
        <div className="space-y-2 flex-1">
            <label className={labelClass}>{label}</label>
            <p>{value || "—"}</p>
        </div>
    );
}
