/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AutoTranslatedSampleAppTestModule } from '../../../test.module';
import { WishListDetailComponent } from 'app/entities/wish-list/wish-list-detail.component';
import { WishList } from 'app/shared/model/wish-list.model';

describe('Component Tests', () => {
    describe('WishList Management Detail Component', () => {
        let comp: WishListDetailComponent;
        let fixture: ComponentFixture<WishListDetailComponent>;
        const route = ({ data: of({ wishList: new WishList(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AutoTranslatedSampleAppTestModule],
                declarations: [WishListDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WishListDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WishListDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.wishList).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
