export default function StatCard({ title, text }) {
    return (
        <div className="flex flex-col items-start gap-2 text-center border-t-2 border-primary py-2 w-full max-w-[300px] mx-auto">
            <h3 className="text-2xl font-bold uppercase text-primary w-full">{title}</h3>
            <p className="font-medium text-sm text-gray-300 w-full">{text}</p>
        </div>
    );
}
