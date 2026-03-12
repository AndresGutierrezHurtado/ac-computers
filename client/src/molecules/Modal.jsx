"use client";

export default function Modal({ title, open, onClose, children, footer }) {
    if (!open) return null;

    return (
        <dialog className="modal" open>
            <div className="modal-box max-w-3xl max-h-[90vh] overflow-y-hidden flex flex-col gap-2">
                <div className="flex items-center justify-between">
                    <h3 className="font-extrabold text-2xl tracking-tight">{title}</h3>
                    <button
                        type="button"
                        className="btn btn-sm btn-circle btn-ghost"
                        onClick={onClose}
                    >
                        ✕
                    </button>
                </div>
                <div className="pt-4 grow overflow-y-auto">{children}</div>
                {footer ? <div className="pt-4">{footer}</div> : null}
            </div>
            <form method="dialog" className="modal-backdrop bg-black/50 backdrop-blur-[1px]">
                <button type="button" onClick={onClose}>
                    close
                </button>
            </form>
        </dialog>
    );
}
