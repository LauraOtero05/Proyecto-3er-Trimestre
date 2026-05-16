class Producto {
    
    constructor(id, titulo, artista, genero, formato, precio, stock, portada){
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.formato = formato;
        this.precio = Number(precio);
        this.stock = Number(stock);
        this.portada = portada || "https://images.pexels.com/photos/36534561/pexels-photo-36534561.jpeg";
    }

    createCardHTML() {
        return `
            <article class="p-disc__card outerbox" data-id="${this.id}">
                <div class="p-image__container">
                    <img class="p-disc__image" src="${this.portada}" alt="Portada de ${this.titulo}">
                    <span class="p-disc__format">${this.formato}</span>
                </div>
                <div class="p-discCard__content">
                    <h3 class="p-disc__title">${this.titulo}</h3>
                    <p class="p-disc__artist">${this.artista}</p>
                    <div class="p-card__data">
                        <span class="p-card__stock">Stock: ${this.stock} u.</span>
                        <span class="p-card__price">${this.precio.toFixed(2)}€</span>
                    </div>
                    <div class="p-card__actions">
                        <button class="p-card__edit">Editar</button>
                        <button class="p-card__delete" data-id="${this.id}">Eliminar</button>
                    </div>
                </div>
            </article>
        `;
    }
}

