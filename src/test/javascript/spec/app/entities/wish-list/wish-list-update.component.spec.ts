/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AutoTranslatedSampleAppTestModule } from '../../../test.module';
import { WishListUpdateComponent } from 'app/entities/wish-list/wish-list-update.component';
import { WishListService } from 'app/entities/wish-list/wish-list.service';
import { WishList } from 'app/shared/model/wish-list.model';

describe('Component Tests', () => {
    describe('WishList Management Update Component', () => {
        let comp: WishListUpdateComponent;
        let fixture: ComponentFixture<WishListUpdateComponent>;
        let service: WishListService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AutoTranslatedSampleAppTestModule],
                declarations: [WishListUpdateComponent]
            })
                .overrideTemplate(WishListUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WishListUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WishListService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new WishList(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.wishList = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new WishList();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.wishList = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
