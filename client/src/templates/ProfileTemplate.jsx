export default function ProfileTemplate({ children }) {
    return (
        <div className="w-full px-4">
            <div className="w-full max-w-[1200px] mx-auto py-10 mt-25">
                {children}
            </div>
        </div>
    );
}
