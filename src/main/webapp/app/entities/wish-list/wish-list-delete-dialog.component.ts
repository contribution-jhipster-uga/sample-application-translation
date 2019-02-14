import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWishList } from 'app/shared/model/wish-list.model';
import { WishListService } from './wish-list.service';

@Component({
    selector: 'jhi-wish-list-delete-dialog',
    templateUrl: './wish-list-delete-dialog.component.html'
})
export class WishListDeleteDialogComponent {
    wishList: IWishList;

    constructor(protected wishListService: WishListService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wishListService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'wishListListModification',
                content: 'Deleted an wishList'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wish-list-delete-popup',
    template: ''
})
export class WishListDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wishList }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WishListDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.wishList = wishList;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/wish-list', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/wish-list', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
