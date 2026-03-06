export default function AdminDetailTemplate({ title, left, right }) {
    return (
        <div className="space-y-10">
            <h2 className="text-4xl font-extrabold tracking-tight">{title}</h2>
            <div className="flex flex-col md:flex-row gap-10">
                {left}
                {right}
            </div>
        </div>
    );
}
