class Proveedor {

    constructor(id, name, phone, email, city, country, status = "Activo") {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.country = country;
        this.status = status;
    }

    createRowHTML() {

    const statusClass = this.status.toLowerCase() === "activo" ? "status--active" : "status--inactive";

    return `
        <div class="s-tableBox s-table__row">
            <div class="s-table__item s-item__id">${this.id}</div>
            <div class="s-table__item s-item__name">${this.name}</div>
            <div class="s-table__item s-item__mvl">${this.phone}</div>
            <div class="s-table__item s-item__email">${this.email}</div>
            <div class="s-table__item s-item__city">${this.city}</div>
            <div class="s-table__item s-item__pais">${this.country}</div>
            <div class="s-table__item s-item__status">
                <span class="status-badge ${statusClass}">${this.status}</span>
            </div>
            <div class="s-table__item s-item__actions">
                <button class="s-btn__edit" data-id="${this.id}"><span class="material-symbols-rounded" data-id="${this.id}">edit</span></button>
                <button class="s-btn__delete" data-id="${this.id}"><span class="material-symbols-rounded" data-id="${this.id}">delete</span></button>
            </div>
        </div>
    `;
}


}