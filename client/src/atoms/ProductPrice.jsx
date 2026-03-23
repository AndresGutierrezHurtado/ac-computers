export default function ProductPrice({ price, discount = 0, size = 1.25 }) {
    const formatter = new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        minimumFractionDigits: 0,
    });

    const finalPrice = discount > 0 ? price - price * (discount / 100) : price;

    return (
        <>
            {discount > 0 && (
                <p className="line-through text-base-content/60" style={{ fontSize: `${size * 0.7}rem` }}>{formatter.format(price)}</p>
            )}
            <p className="font-medium" style={{ fontSize: `${size}rem` }}>
                {formatter.format(finalPrice)}
            </p>
        </>
    );
}
