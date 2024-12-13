const {
    addUserHandler,
    getAllUsersHandler,
    getUserByIdHandler,
    updateUserByIdHandler,
    deleteUserByIdHandler,
    loginUserHandler
} = require('./handler');

const userRoutes = [
    {
        method: 'POST',
        path: '/users',
        handler: addUserHandler,
    },
    {
        method: 'GET',
        path: '/users',
        handler: getAllUsersHandler,
    },
    {
        method: 'GET',
        path: '/users/{userId}',
        handler: getUserByIdHandler,
    },
    {
        method: 'PUT',
        path: '/users/{userId}',
        handler: updateUserByIdHandler,
    },
    {
        method: 'DELETE',
        path: '/users/{userId}',
        handler: deleteUserByIdHandler,
    },
    {
        method: 'POST',
        path: '/users/login',
        handler: loginUserHandler, 
    },
];

module.exports = userRoutes;
