const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
const port = 3000;

app.use(express.json());

const dataFilePath = path.join(__dirname, 'data', 'products.json');

app.get('/', (req, res) => {
  res.send('Server running');
});

// POST /products/ route handler
app.post('/products/', (req, res) => {
    fs.readFile(dataFilePath, (err, data) => {
        console.log("fs.readFile error:", err); // Added log for fs.readFile error

        if (err && err.code === 'ENOENT') {
            fs.writeFileSync(dataFilePath, '[]');
            data = '[]';
            err = null;
        }

        let products = [];
        console.log("Data before parse:", data); // Added log for data before parsing

        if (!err) {
            try {
                if (data && data.length > 0) {
                    products = JSON.parse(data);
                } else {
                    products = [];
                }
                console.log("Products after parse:", products); // Added log for products after parsing
            } catch (parseError) {
                console.error("Error parsing JSON:", parseError);
                return res.status(500).json({ error: 'Invalid product data in file' });
            }
        } else {
            console.error("Error reading file:", err);
            return res.status(500).json({ error: 'Failed to create product' });
        }

        const newProduct = req.body;
        console.log("Received newProduct:", newProduct);

        if (!newProduct.title || !newProduct.Price || !newProduct.Description || !newProduct["Image URL"] || !newProduct.Category) {
            return res.status(400).json({ error: 'Missing required fields' });
        }

        const price = parseFloat(newProduct.Price);
        if (isNaN(price)) {
            return res.status(400).json({ error: 'Price must be a valid number' });
        }
        console.log("Price after parsing:", price);

        const productId = Date.now();

        const product = {
            id: productId,
            title: newProduct.title,
            Price: price,
            Description: newProduct.Description,
            "Image URL": newProduct["Image URL"],
            Category: newProduct.Category,
        };
        console.log("Product object to be saved:", product);

        products.push(product);

        fs.writeFile(dataFilePath, JSON.stringify(products, null, 2), (err) => {
            if (err) {
                console.error("Error saving product:", err);
                return res.status(500).json({ error: 'Failed to create product' });
            }
            console.log("Product saved successfully:", product);
            res.status(201).json(product);
        });
    });
});
// GET /products/:id route handler
app.get('/products/:id', (req, res) => {
    const productId = parseInt(req.params.id);

    fs.readFile(dataFilePath, (err, data) => {
        if (err) {
            console.error("Error reading file:", err);
            return res.status(500).json({ error: 'Failed to get product' });
        }

        const products = JSON.parse(data);
        const product = products.find(p => p.id === productId);

         if (!product) {
            return res.status(404).json({ error: 'Product not found' });
        }

        res.status(200).json(product);
    });
});

app.put('/products/:id', (req, res) => {
    const productId = parseInt(req.params.id);

    // Read the updates from Updateproduct.json
    fs.readFile(path.join(__dirname, 'Updateproduct.json'), (updateErr, updateData) => {
        if (updateErr) {
            console.error("Error reading Updateproduct.json:", updateErr);
            return res.status(500).json({ error: 'Failed to read update data' });
        }

        let updates;
        try {
            updates = JSON.parse(updateData);
        } catch (parseError) {
            console.error("Error parsing Updateproduct.json:", parseError);
            return res.status(500).json({ error: 'Invalid update data' });
        }

        // Read the products from products.json
        fs.readFile(dataFilePath, (err, data) => {
            if (err && err.code === 'ENOENT') {
                fs.writeFileSync(dataFilePath, '[]');
                data = '[]';
                err = null;
            }

            let products = [];
            try {
                if (data) {
                    products = JSON.parse(data);
                } else {
                    products = [];
                }
            } catch (parseError) {
                console.error("Error parsing products.json:", parseError);
                return res.status(500).json({ error: 'Invalid product data in file' });
            }

            const productIndex = products.findIndex(p => p.id === productId);

            if (productIndex === -1) {
                return res.status(404).json({ error: 'Product not found' });
            }

            const productToUpdate = products[productIndex];

            // Apply updates from Updateproduct.json
            if (updates.title) productToUpdate.title = updates.title;
            if (updates.Price) productToUpdate.Price = parseFloat(updates.Price);
            if (updates.Description) productToUpdate.Description = updates.Description;
            if (updates["Image URL"]) productToUpdate["Image URL"] = updates["Image URL"];
            if (updates.Category) productToUpdate.Category = updates.Category;

            products[productIndex] = productToUpdate;

            fs.writeFile(dataFilePath, JSON.stringify(products, null, 2), (err) => {
                if (err) {
                    console.error("Error writing file:", err);
                    return res.status(500).json({ error: 'Failed to update product' });
                }
                res.json(productToUpdate);
            });
        });
    });
});

app.delete('/products', (req, res) => {
    console.log("Delete products route called"); // add this line
    fs.writeFileSync(dataFilePath, '[]');
    res.status(200).send("products.json cleared");
});

app.listen(port, () => {
    console.log(`Custom mock server is running on http://localhost:${port}`);
});