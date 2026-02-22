export default function ProductPrice({ price, discount = 0 }) {
    const formatter = new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0,
    });

    const finalPrice = discount > 0 ? price - price * (discount / 100) : price;

    return (
        <>
            {discount > 0 && (
                <p className="line-through text-gray-400">{formatter.format(price)}</p>
            )}
            <p className="font-bold text-lg">{formatter.format(finalPrice)}</p>
        </>
    );
}
