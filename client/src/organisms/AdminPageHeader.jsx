"use client";

export default function AdminPageHeader({ title, action }) {
    return (
        <div className="flex justify-between items-center w-full">
            <h1 className="text-3xl font-bold mb-4">{title}</h1>
            {action}
        </div>
    );
}
