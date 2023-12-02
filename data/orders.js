db.createCollection('orders');

db = db.getSiblingDB('orders');

db.orders.insertMany([
    {
        _id: ObjectId('6536518de9b0d305f193b5ef'),
        idUser: 1,
        client: {
            id:1,
            username: 'user1',
            name: 'John',
            balance: 100.00,
            email: 'john@clownsinformatics.com',
            address: '1234 Main St',
            phone: '673 456 893',
            birthdate: '1990-01-01',
            image: 'https://i.imgur.com/4M34hi2.jpg',
            isDeleted: false,
            createdAt:new Date(),
            updatedAt:new Date()
        },
        orderLines: [
            {
                idProduct: '3512e012-7028-405c-8397-b39886006212',
                quantity: 1,
                price: 19.99,
                total: 19.99,
            },
            {
                idProduct: '76549b87-23a2-4065-8a86-914207290329',
                quantity: 2,
                price: 15.99,
                total: 31.98,
            },
        ],
        createdAt: '2023-10-23T12:57:17.3411925',
        updatedAt: '2023-10-23T12:57:17.3411925',
        isDeleted: false
    },
]);