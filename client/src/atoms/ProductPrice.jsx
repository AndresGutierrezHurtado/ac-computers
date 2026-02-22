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
                <p className="line-through text-base-content/60">{formatter.format(price)}</p>
            )}
            <p className="font-medium text-lg">{formatter.format(finalPrice)}</p>
        </>
    );
}
