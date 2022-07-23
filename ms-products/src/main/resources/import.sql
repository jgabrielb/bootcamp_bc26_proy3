db.getSiblingDB("bdbootcamp")
    .getCollection("schema_products.products").deleteMany({});

db.getSiblingDB("bdbootcamp").getCollection("schema_products.products").insertMany( [
    { indProduct: 1, descIndProduct: "PASIVOS", typeProduct: 1, descTypeProduct: "CUENTA DE AHORROS" },
    { indProduct: 1, descIndProduct: "PASIVOS", typeProduct: 2, descTypeProduct: "CUENTA CORRIENTE" },
    { indProduct: 1, descIndProduct: "PASIVOS", typeProduct: 3, descTypeProduct: "CUENTA PLAZO FIJO" },
    { indProduct: 2, descIndProduct: "ACTIVOS", typeProduct: 1, descTypeProduct: "CREDITO PERSONAL" },
    { indProduct: 2, descIndProduct: "ACTIVOS", typeProduct: 2, descTypeProduct: "CREDITO EMPRESARIAL" },
    { indProduct: 2, descIndProduct: "ACTIVOS", typeProduct: 3, descTypeProduct: "TARJETA DE CREDITO" }
])