import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AutoTranslatedSampleAppSharedModule } from 'app/shared';
import {
    WishListComponent,
    WishListDetailComponent,
    WishListUpdateComponent,
    WishListDeletePopupComponent,
    WishListDeleteDialogComponent,
    wishListRoute,
    wishListPopupRoute
} from './';

const ENTITY_STATES = [...wishListRoute, ...wishListPopupRoute];

@NgModule({
    imports: [AutoTranslatedSampleAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WishListComponent,
        WishListDetailComponent,
        WishListUpdateComponent,
        WishListDeleteDialogComponent,
        WishListDeletePopupComponent
    ],
    entryComponents: [WishListComponent, WishListUpdateComponent, WishListDeleteDialogComponent, WishListDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AutoTranslatedSampleAppWishListModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
