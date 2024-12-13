const db = require('../firestore'); // Impor Firestore

const addUserHandler = async (req, h) => {
    const { name, userName, email, password } = req.payload;

    if (!name || !userName || !email || !password) {
        return h.response({
            status: 'fail',
            message: 'All fields (name, userName, email, password) are required'
        }).code(400);
    }

    // Menambahkan pengguna tanpa menentukan ID, Firestore akan membuat ID unik
    const userRef = await db.collection('users').add({
        name,
        userName,
        email,
        password
    });

    // Mengambil ID dokumen yang baru saja dibuat
    const userId = userRef.id;

    return h.response({
        status: 'success',
        message: 'User added successfully',
        data: { userId }
    }).code(201);
};

const getAllUsersHandler = async (req, h) => {
    const snapshot = await db.collection('users').get();
    const users = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));

    return h.response({
        status: 'success',
        data: { users }
    }).code(200);
};

const getUserByIdHandler = async (req, h) => {
    const { userId } = req.params;
    const userRef = db.collection('users').doc(userId);
    const doc = await userRef.get();

    if (!doc.exists) {
        return h.response({
            status: 'fail',
            message: 'User not found'
        }).code(404);
    }

    return h.response({
        status: 'success',
        data: { user: doc.data() }
    }).code(200);
};

const updateUserByIdHandler = async (req, h) => {
    const { userId } = req.params;
    const { name, userName, email, password } = req.payload;
    const userRef = db.collection('users').doc(userId);

    const doc = await userRef.get();
    if (!doc.exists) {
        return h.response({
            status: 'fail',
            message: 'User not found'
        }).code(404);
    }

    await userRef.update({
        name,
        userName,
        email,
        password
    });

    return h.response({
        status: 'success',
        message: 'User updated successfully'
    }).code(200);
};

const deleteUserByIdHandler = async (req, h) => {
    const { userId } = req.params;
    const userRef = db.collection('users').doc(userId);

    const doc = await userRef.get();
    if (!doc.exists) {
        return h.response({
            status: 'fail',
            message: 'User not found'
        }).code(404);
    }

    await userRef.delete();

    return h.response({
        status: 'success',
        message: 'User deleted successfully'
    }).code(200);
};

// Fungsi baru untuk login user
const loginUserHandler = async (req, h) => {
    const { email, password } = req.payload;

    if (!email || !password) {
        return h.response({
            status: 'fail',
            message: 'Email and password are required'
        }).code(400);
    }

    try {
        // Cari pengguna berdasarkan email dan password
        const usersRef = db.collection('users');
        const querySnapshot = await usersRef
            .where('email', '==', email)
            .where('password', '==', password)
            .get();

        if (querySnapshot.empty) {
            // Jika tidak ada pengguna yang cocok
            return h.response({
                status: 'fail',
                message: 'Invalid email or password'
            }).code(401);
        }

        // Jika pengguna ditemukan
        const user = querySnapshot.docs[0].data();
        return h.response({
            status: 'success',
            message: 'User logged in successfully',
            data: { userId: querySnapshot.docs[0].id, email: user.email, name: user.name }
        }).code(200);
    } catch (error) {
        console.error('Error logging in: ', error);
        return h.response({
            status: 'error',
            message: 'Failed to log in'
        }).code(500);
    }
};

module.exports = {
    addUserHandler,
    getAllUsersHandler,
    getUserByIdHandler,
    updateUserByIdHandler,
    deleteUserByIdHandler,
    loginUserHandler 
};