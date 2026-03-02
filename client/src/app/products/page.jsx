import ProductsMarketplace from "@/organisms/ProductsMarketplace";
import AIChatWidget from "@/organisms/AIChatWidget";

export const metadata = {
    title: "Productos | AC Computers",
};

export default function ProductsPage() {
    return (
        <>
            <ProductsMarketplace />
            <AIChatWidget />
        </>
    );
}
