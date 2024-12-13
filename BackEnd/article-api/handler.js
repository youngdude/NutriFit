const db = require('../firestore'); // Mengimpor Firestore instance dari firestore.js

// Handler untuk menambahkan artikel baru
const addArticleHandler = async(req, h) => {
    const { content, thumbnail, title } = req.payload;

    // Validasi bahwa semua field terisi
    if (!content || !thumbnail || !title) {
        return h.response({
            status: 'fail',
            message: 'All fields (content, thumbnail, title) are required'
        }).code(400);
    }

    // Menambahkan artikel dengan ID otomatis dari Firestore
    const articleRef = await db.collection('articles').add({
        content,
        thumbnail,
        title
    });

    // Mengambil ID dokumen yang baru saja dibuat
    const articleId = articleRef.id;

    return h.response({
        status: 'success',
        message: 'Article added successfully',
        data: { articleId }
    }).code(201);
};

// Handler untuk mendapatkan semua artikel
const getAllArticlesHandler = async(req, h) => {
    const snapshot = await db.collection('articles').get();
    const articles = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
    }));

    return h.response({
        status: 'success',
        data: { articles }
    }).code(200);
};

// Handler untuk mendapatkan artikel berdasarkan ID
const getArticleByIdHandler = async(req, h) => {
    const { articleId } = req.params;
    const articleRef = db.collection('articles').doc(articleId);
    const doc = await articleRef.get();

    if (!doc.exists) {
        return h.response({
            status: 'fail',
            message: 'Article not found'
        }).code(404);
    }

    return h.response({
        status: 'success',
        data: { article: doc.data() }
    }).code(200);
};

// Handler untuk memperbarui artikel berdasarkan ID
const updateArticleByIdHandler = async(req, h) => {
    const { articleId } = req.params;
    const { content, thumbnail, title } = req.payload;
    const articleRef = db.collection('articles').doc(articleId);

    // Memeriksa apakah artikel dengan ID yang diberikan ada
    const doc = await articleRef.get();
    if (!doc.exists) {
        return h.response({
            status: 'fail',
            message: 'Article not found'
        }).code(404);
    }

    // Memperbarui artikel
    await articleRef.update({
        content,
        thumbnail,
        title
    });

    return h.response({
        status: 'success',
        message: 'Article updated successfully'
    }).code(200);
};

// Handler untuk menghapus artikel berdasarkan ID
const deleteArticleByIdHandler = async(req, h) => {
    const { articleId } = req.params;
    const articleRef = db.collection('articles').doc(articleId);

    // Memeriksa apakah artikel dengan ID yang diberikan ada
    const doc = await articleRef.get();
    if (!doc.exists) {
        return h.response({
            status: 'fail',
            message: 'Article not found'
        }).code(404);
    }

    // Menghapus artikel
    await articleRef.delete();

    return h.response({
        status: 'success',
        message: 'Article deleted successfully'
    }).code(200);
};

module.exports = {
    addArticleHandler,
    getAllArticlesHandler,
    getArticleByIdHandler,
    updateArticleByIdHandler,
    deleteArticleByIdHandler
};