import FormattedNumberInput from "@/atoms/FormattedNumberInput";
import { CloseIcon } from "@/atoms/Icons";

export default function ProductFiltersPanel({
    filters,
    categories = [],
    subCategories = [],
    brands = [],
    conditions = [],
    onCategoryChange,
    onSubCategoryChange,
    onBrandChange,
    onConditionChange,
    onMinPriceChange,
    onMaxPriceChange,
    onMinDiscountChange,
    onMaxDiscountChange,
    onPerPageChange,
    onReset,
}) {
    return (
        <div className="card bg-base-200/40">
            <div className="card-body gap-4">
                <h2 className="card-title text-3xl italic font-bold scale-y-105 text-primary mb-4">Filtros</h2>

                <div className="space-y-2">
                    <p className="text-sm font-semibold">Categoria</p>
                    <select
                        className="select select-sm select-bordered w-full"
                        value={filters.categoryId}
                        onChange={(e) => onCategoryChange(e.target.value)}
                    >
                        <option value="">Todas</option>
                        {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="space-y-2">
                    <p className="text-sm font-semibold">Subcategoria</p>
                    <select
                        className="select select-sm select-bordered w-full"
                        value={filters.subCategoryId}
                        onChange={(e) => onSubCategoryChange(e.target.value)}
                    >
                        <option value="">Todas</option>
                        {subCategories.map((subCategory) => (
                            <option key={subCategory.id} value={subCategory.id}>
                                {subCategory.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="space-y-2">
                    <p className="text-sm font-semibold">Marca</p>
                    <select
                        className="select select-sm select-bordered w-full"
                        value={filters.brandId}
                        onChange={(e) => onBrandChange(e.target.value)}
                    >
                        <option value="">Todas</option>
                        {brands.map((brand) => (
                            <option key={brand.id} value={brand.id}>
                                {brand.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="space-y-2">
                    <p className="text-sm font-semibold">Condicion</p>
                    <select
                        className="select select-sm select-bordered w-full"
                        value={filters.condition}
                        onChange={(e) => onConditionChange(e.target.value)}
                    >
                        <option value="">Todas</option>
                        {conditions.map((condition) => (
                            <option key={condition.value} value={condition.value}>
                                {condition.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="space-y-2">
                    <p className="text-sm font-semibold">Precio</p>
                    <div className="flex gap-2">
                        <FormattedNumberInput
                            placeholder="Min"
                            value={filters.minPrice}
                            onChange={onMinPriceChange}
                        />
                        <FormattedNumberInput
                            placeholder="Max"
                            value={filters.maxPrice}
                            onChange={onMaxPriceChange}
                        />
                    </div>
                </div>

                <div className="space-y-2">
                    <p className="text-sm font-semibold">Descuento (%)</p>
                    <div className="flex gap-2">
                        <FormattedNumberInput
                            placeholder="Min"
                            value={filters.minDiscount}
                            onChange={onMinDiscountChange}
                        />
                        <FormattedNumberInput
                            placeholder="Max"
                            value={filters.maxDiscount}
                            onChange={onMaxDiscountChange}
                        />
                    </div>
                </div>

                <div className="space-y-2">
                    <p className="text-sm font-semibold">Resultados por pagina</p>
                    <select
                        className="select select-sm select-bordered w-full"
                        value={filters.perPage}
                        onChange={(e) => onPerPageChange(Number(e.target.value))}
                    >
                        <option value={12}>12</option>
                        <option value={24}>24</option>
                        <option value={48}>48</option>
                    </select>
                </div>

                <button type="button" className="btn btn-outline" onClick={onReset}>
                    <CloseIcon />
                    Limpiar filtros
                </button>
            </div>
        </div>
    );
}
