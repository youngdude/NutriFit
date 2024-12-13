const Hapi = require('@hapi/hapi');
const userRoutes = require('./user-api/routes');
const articleRoutes = require('./article-api/routes');

const init = async() => {
    const server = Hapi.server({
        port: process.env.PORT || 8000, // Gunakan PORT dari lingkungan
        host: '0.0.0.0', // Host harus 0.0.0.0 untuk App Engine
    });

    // Tambahkan rute
    server.route([...userRoutes, ...articleRoutes]);

    // Mulai server
    await server.start();
    console.log(`Server berjalan pada ${server.info.uri}`);
};

init();