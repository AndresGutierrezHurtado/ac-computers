"use client";

export default function ChatComposer({
    value,
    onChange,
    onSubmit,
    onKeyDown,
    loading,
    placeholder = "Escribe tu pregunta...",
}) {
    return (
        <form onSubmit={onSubmit} className="flex items-end gap-2">
            <textarea
                className="textarea textarea-bordered w-full resize-none h-20 focus:outline-0 focus:border-primary bg-transparent"
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                onKeyDown={onKeyDown}
                disabled={loading}
            />
            <button
                type="submit"
                className="btn btn-primary"
                disabled={loading || !value.trim()}
            >
                Enviar
            </button>
        </form>
    );
}
