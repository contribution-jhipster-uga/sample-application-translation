import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IWishList } from 'app/shared/model/wish-list.model';
import { AccountService } from 'app/core';
import { WishListService } from './wish-list.service';

@Component({
    selector: 'jhi-wish-list',
    templateUrl: './wish-list.component.html'
})
export class WishListComponent implements OnInit, OnDestroy {
    wishLists: IWishList[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected wishListService: WishListService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.wishListService
            .query()
            .pipe(
                filter((res: HttpResponse<IWishList[]>) => res.ok),
                map((res: HttpResponse<IWishList[]>) => res.body)
            )
            .subscribe(
                (res: IWishList[]) => {
                    this.wishLists = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWishLists();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWishList) {
        return item.id;
    }

    registerChangeInWishLists() {
        this.eventSubscriber = this.eventManager.subscribe('wishListListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
