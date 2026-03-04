export default function AuthSplitTemplate({ left, right, reverse = false }) {
    return (
        <div className="hero bg-base-200 min-h-screen">
            <div
                className={`hero-content flex-col ${
                    reverse ? "lg:flex-row-reverse" : "lg:flex-row"
                } gap-[50px] z-[1]`}
            >
                {left}
                {right}
            </div>
        </div>
    );
}
