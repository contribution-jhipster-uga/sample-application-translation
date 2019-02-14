import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IWishList } from 'app/shared/model/wish-list.model';
import { WishListService } from './wish-list.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
    selector: 'jhi-wish-list-update',
    templateUrl: './wish-list-update.component.html'
})
export class WishListUpdateComponent implements OnInit {
    wishList: IWishList;
    isSaving: boolean;

    customers: ICustomer[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected wishListService: WishListService,
        protected customerService: CustomerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wishList }) => {
            this.wishList = wishList;
        });
        this.customerService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICustomer[]>) => response.body)
            )
            .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.wishList.id !== undefined) {
            this.subscribeToSaveResponse(this.wishListService.update(this.wishList));
        } else {
            this.subscribeToSaveResponse(this.wishListService.create(this.wishList));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IWishList>>) {
        result.subscribe((res: HttpResponse<IWishList>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }
}
