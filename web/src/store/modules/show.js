import { defineStore } from 'pinia'
export const showStore = defineStore(
    'show',
    {
    state: () => ({
        
        socket: null,  
    }),
    getters: {
    },
    actions: {
        updateSocket(socket) {
            this.socket = socket;
        },

    },

    }
)
