import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseAssignment } from './course-assignment';

describe('CourseAssignment', () => {
  let component: CourseAssignment;
  let fixture: ComponentFixture<CourseAssignment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CourseAssignment],
    }).compileComponents();

    fixture = TestBed.createComponent(CourseAssignment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
