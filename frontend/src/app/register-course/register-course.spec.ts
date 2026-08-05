import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterCourse } from './register-course';

describe('RegisterCourse', () => {
  let component: RegisterCourse;
  let fixture: ComponentFixture<RegisterCourse>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterCourse],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterCourse);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
