import PageHeader from "@/molecules/PageHeader";

export default function ProductListTemplate({ title, searchPlaceholder, searchValue, searchOnChange, children }) {
    return (
        <main className="w-full">
            <section className="w-full px-3 mt-[100px]">
                <div className="w-full max-w-[1200px] mx-auto flex flex-col gap-10">
                    <PageHeader
                        title={title}
                        searchPlaceholder={searchPlaceholder}
                        searchValue={searchValue}
                        searchOnChange={searchOnChange}
                    />
                    <div className="grid grid-cols-[repeat(auto-fill,minmax(250px,1fr))] gap-14">
                        {children}
                    </div>
                </div>
            </section>
        </main>
    );
}
