const admin = require('firebase-admin');
const serviceAccount = require('./config/firebase-credentials.json');

// Inisialisasi aplikasi Firebase dengan credential
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

module.exports = db;