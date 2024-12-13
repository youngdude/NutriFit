const { auth } = require('../firestore');

// Login handler tetap sama seperti yang diperbaiki sebelumnya
const loginUser = async(request, h) => {
    const { email, password } = request.payload;

    if (!email || !password) {
        return h.response({ error: 'Email and password are required' }).code(400);
    }

    try {
        const userCredential = await auth.signInWithEmailAndPassword(email, password);
        const user = userCredential.user;
        const token = await user.getIdToken();
        return h.response({
            message: 'User logged in successfully',
            uid: user.uid,
            token: token,
        }).code(200);
    } catch (error) {
        let errorMessage = 'Login failed';
        if (error.code === 'auth/user-not-found') {
            errorMessage = 'User not found';
        } else if (error.code === 'auth/wrong-password') {
            errorMessage = 'Incorrect password';
        }
        return h.response({ error: errorMessage }).code(400);
    }
};

module.exports = [
    { method: 'POST', path: '/user-login', handler: loginUser },
];