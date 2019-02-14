/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AutoTranslatedSampleAppTestModule } from '../../../test.module';
import { WishListComponent } from 'app/entities/wish-list/wish-list.component';
import { WishListService } from 'app/entities/wish-list/wish-list.service';
import { WishList } from 'app/shared/model/wish-list.model';

describe('Component Tests', () => {
    describe('WishList Management Component', () => {
        let comp: WishListComponent;
        let fixture: ComponentFixture<WishListComponent>;
        let service: WishListService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AutoTranslatedSampleAppTestModule],
                declarations: [WishListComponent],
                providers: []
            })
                .overrideTemplate(WishListComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WishListComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WishListService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new WishList(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.wishLists[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
