export const metadata = {
    title: "Computadores | AC Computers",
};

export default function ComputersPage() {
    return (
        <main className="w-full">
            <section className="w-full px-3 mt-[100px]">
                <div className="w-full max-w-[1200px] mx-auto flex flex-col gap-10">
                    <div className="flex flex-col sm:flex-row items-start sm:items-end gap-4 justify-between">
                        <h2 className="text-4xl font-extrabold tracking-tight">
                            Lista computadores:
                        </h2>
                        <form action="/computers" method="get" className="w-full max-w-sm">
                            <label className="input input-sm input-bordered focus-within:outline-0 focus-within:input-primary flex items-center gap-2 w-full">
                                <input
                                    type="text"
                                    className="grow group"
                                    placeholder="Buscar computadores"
                                    name="search"
                                />
                                <button type="submit">🔍</button>
                            </label>
                        </form>
                    </div>

                    <div className="grid grid-cols-[repeat(auto-fill,minmax(250px,1fr))] gap-14">
                        {/* Products will be rendered here */}
                    </div>
                </div>
            </section>
        </main>
    );
}
