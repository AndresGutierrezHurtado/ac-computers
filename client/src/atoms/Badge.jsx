export default function Badge({ children }) {
    return (
        <div className="badge bg-primary/20 text text-primary font-medium border border-primary/50 mb-1">
            {children}
        </div>
    );
}
