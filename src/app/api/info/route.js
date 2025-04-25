import { NextResponse } from "next/server";

// Database models
import { User, Role, Category, Product, Spec, Multimedia, Recovery } from "@/database/models";

export async function GET(request) {
    try {
        const users = await User.findAll();
        const roles = await Role.findAll();
        const categories = await Category.findAll();
        const products = await Product.findAll();
        const specs = await Spec.findAll();
        const multimedias = await Multimedia.findAll();
        const recoveries = await Recovery.findAll();

        return NextResponse.json(
            { categories, roles, users, products, specs, multimedias, recoveries },
            { status: 200 }
        );
    } catch (error) {
        console.error(error.message);
        return NextResponse.json(
            {
                success: false,
                message: "Error al obtener información: " + error.message,
            },
            { status: 500 }
        );
    }
}
