import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'category',
                loadChildren: './category/category.module#AutoTranslatedSampleAppCategoryModule'
            },
            {
                path: 'product',
                loadChildren: './product/product.module#AutoTranslatedSampleAppProductModule'
            },
            {
                path: 'customer',
                loadChildren: './customer/customer.module#AutoTranslatedSampleAppCustomerModule'
            },
            {
                path: 'address',
                loadChildren: './address/address.module#AutoTranslatedSampleAppAddressModule'
            },
            {
                path: 'wish-list',
                loadChildren: './wish-list/wish-list.module#AutoTranslatedSampleAppWishListModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AutoTranslatedSampleAppEntityModule {}
